package net.binggl.login.core.entity;

import java.io.Serializable;
import java.util.Date;

import org.bson.types.ObjectId;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.PrePersist;
import org.mongodb.morphia.annotations.Property;

/**
 * basic mongo entity
 * @author henrik
 *
 */
public abstract class BaseEntity implements Serializable {
	private static final long serialVersionUID = -1714215220872785110L;

	@Id
	@Property("_id")
	protected ObjectId id;

    @Property("created")
    protected Date created;
    @Property("modified")
    protected Date modified;


    // getters / setters


    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getModified() {
        return modified;
    }

    public void setModified(Date modified) {
        this.modified = modified;
    }

    public String getId() {
        return this.id.toHexString();
    }
    
    public void setId(String id) {
        this.id = new ObjectId(id);
    }


    // lifecycle actions

    /**
     * set fields before save
     */
    @PrePersist
    protected void prePersist() {
        if(this.created == null) {
            this.created = new Date();
        }
        this.modified = new Date();
    }
}