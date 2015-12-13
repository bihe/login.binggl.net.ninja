package controllers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Optional;
import com.google.common.io.ByteStreams;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import filters.SecurityFilter;
import ninja.AssetsController;
import ninja.AssetsControllerHelper;
import ninja.Context;
import ninja.FilterWith;
import ninja.Renderable;
import ninja.Result;
import ninja.Results;
import ninja.utils.HttpCacheToolkit;
import ninja.utils.MimeTypes;
import ninja.utils.NinjaProperties;
import ninja.utils.ResponseStreams;

/**
 * Subclass the AssetsController. Add a security logic to the asset controller
 * enable serving of assets from arbitrary file-system path in production! the
 * external filesystem handling was "back-ported" from ninjaframework the
 * feature was removed due to security considerations --> see:
 * https://github.com/ninjaframework/ninja/commit/
 * 81d29be2a16f0c261d12d2363db7144dd512634b
 * 
 * @author henrik
 */
@FilterWith(SecurityFilter.class)
@Singleton
public class SecurityAwareAssetsController extends AssetsController {

	private static final Logger logger = LoggerFactory.getLogger(SecurityAwareAssetsController.class);
	final String APPLICATION_STATIC_ASSET_BASEDIR = "application.static.asset.basedir";

	private final MimeTypes mimeTypes;
	private final HttpCacheToolkit httpCacheToolkit;
	private final NinjaProperties ninjaProperties;
	private final AssetsControllerHelper assetsControllerHelper;
	private final Optional<String> assetBaseDir;

	@Inject
	public SecurityAwareAssetsController(AssetsControllerHelper assetsControllerHelper,
			HttpCacheToolkit httpCacheToolkit, MimeTypes mimeTypes, NinjaProperties ninjaProperties) {
		super(assetsControllerHelper, httpCacheToolkit, mimeTypes, ninjaProperties);

		this.mimeTypes = mimeTypes;
		this.httpCacheToolkit = httpCacheToolkit;
		this.ninjaProperties = ninjaProperties;
		this.assetsControllerHelper = assetsControllerHelper;
		this.assetBaseDir = getNormalizedAssetPath(ninjaProperties);

		logger.info("Use custom security aware assets controller!");
	}

	@Override
	public Result serveStatic() {
		Object renderable = new Renderable() {
			@Override
			public void render(Context context, Result result) {
				String fileName = getFileNameFromPathOrReturnRequestPath(context);
				URL url = getStaticFileFromAssetsDir(fileName);
				streamOutUrlEntity(url, context, result);
			}
		};
		return Results.ok().render(renderable);
	}

	private static String getFileNameFromPathOrReturnRequestPath(Context context) {

		String fileName = context.getPathParameter(FILENAME_PATH_PARAM);

		if (fileName == null) {
			fileName = context.getRequestPath();
		}
		return fileName;

	}

	private void streamOutUrlEntity(URL url, Context context, Result result) {
		// check if stream exists. if not print a notfound exception
		if (url == null) {
			context.finalizeHeadersWithoutFlashAndSessionCookie(Results.notFound());
		} else {
			try {
				URLConnection urlConnection = url.openConnection();
				Long lastModified = urlConnection.getLastModified();
				httpCacheToolkit.addEtag(context, result, lastModified);

				if (result.getStatusCode() == Result.SC_304_NOT_MODIFIED) {
					// Do not stream anything out. Simply return 304
					context.finalizeHeadersWithoutFlashAndSessionCookie(result);
				} else {
					result.status(200);

					// Try to set the mimetype:
					String mimeType = mimeTypes.getContentType(context, url.getFile());

					if (mimeType != null && !mimeType.isEmpty()) {
						result.contentType(mimeType);
					}

					ResponseStreams responseStreams = context.finalizeHeadersWithoutFlashAndSessionCookie(result);

					try (InputStream inputStream = urlConnection.getInputStream();
							OutputStream outputStream = responseStreams.getOutputStream()) {
						ByteStreams.copy(inputStream, outputStream);
					}

				}

			} catch (IOException e) {
				logger.error("error streaming file", e);
			}

		}

	}

	private URL getUrlForFile(File possibleFileInSrc) {
		if (possibleFileInSrc.exists() && !possibleFileInSrc.isDirectory()) {
			try {
				return possibleFileInSrc.toURI().toURL();
			} catch (MalformedURLException malformedURLException) {
				logger.error("Error in dev mode while streaming files from src dir. ", malformedURLException);
			}
		}
		return null;
	}

	/**
	 * Loads files from assets directory. This is the default directory of Ninja
	 * where to store stuff. Usually in src/main/java/assets/. But if user wants
	 * to use a dir outside of application project dir, then base dir can be
	 * overridden by static.asset.base.dir in application conf file.
	 */
	private URL getStaticFileFromAssetsDir(String fileName) {

		URL url = null;

		if (ninjaProperties.isDev()
				// Testing that the file exists is important because
				// on some dev environments we do not get the correct asset dir
				// via System.getPropery("user.dir").
				// In that case we fall back to trying to load from classpath
				&& new File(assetsDirInDevModeWithoutTrailingSlash()).exists()) {
			String finalNameWithoutLeadingSlash = assetsControllerHelper.normalizePathWithoutLeadingSlash(fileName,
					false);
			File possibleFile = new File(
					assetsDirInDevModeWithoutTrailingSlash() + File.separator + finalNameWithoutLeadingSlash);
			url = getUrlForFile(possibleFile);
		} else {
			// https://github.com/ninjaframework/ninja/commit/81d29be2a16f0c261d12d2363db7144dd512634b
			// from ninjaframework core
			// Serve from the static asset base directory specified by user in
			// application conf.
			if (assetBaseDir.isPresent()) {
				String finalNameWithoutLeadingSlash = getNormalizePathWithoutLeadingSlash(fileName);
				File possibleFile = new File(assetBaseDir.get() + File.separator + finalNameWithoutLeadingSlash);
				url = getUrlForFile(possibleFile);
			} else {
				String finalNameWithoutLeadingSlash = assetsControllerHelper.normalizePathWithoutLeadingSlash(fileName,
						true);
				url = this.getClass().getClassLoader().getResource(ASSETS_DIR + "/" + finalNameWithoutLeadingSlash);
			}
		}

		return url;
	}

	/**
	 * Used in dev mode: Streaming directly from src dir without jetty reload.
	 */
	private String assetsDirInDevModeWithoutTrailingSlash() {
		String srcDir = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
				+ File.separator + "java";
		return srcDir + File.separator + ASSETS_DIR;
	}

	private Optional<String> getNormalizedAssetPath(NinjaProperties ninjaProperties) {
		String baseDir = ninjaProperties.get(APPLICATION_STATIC_ASSET_BASEDIR);
		return Optional.fromNullable(FilenameUtils.normalizeNoEndSeparator(baseDir));
	}

	/**
	 * If we get - for whatever reason - a relative URL like
	 * assets/../conf/application.conf we expand that to the "real" path. In the
	 * above case conf/application.conf.
	 *
	 * You should then add the assets prefix.
	 *
	 * Otherwise someone can create an attack and read all resources of our app.
	 * If we expand and normalize the incoming path this is no longer possible.
	 *
	 * @param fileName
	 *            A potential "fileName"
	 * @return A normalized fileName.
	 */
	@VisibleForTesting
	protected String getNormalizePathWithoutLeadingSlash(String fileName) {
		String fileNameNormalized = FilenameUtils.normalize(fileName);
		return StringUtils.removeStart(fileNameNormalized, "/");
	}
}
