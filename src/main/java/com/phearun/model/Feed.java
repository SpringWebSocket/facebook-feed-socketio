
package com.phearun.model;

import java.util.UUID;

/**
 * Created by PHEARUN on 12/9/2016.
 */

public class Feed {
    private String id;
    private String text;
    private String username;
    private int like;
    public Feed() {
    	id = UUID.randomUUID().toString();
    }

    public Feed(String text) {
    	id = UUID.randomUUID().toString();
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getLike() {
		return like;
	}

	public void setLike(int like) {
		this.like = like;
	}

	@Override
	public String toString() {
		return "Feed [id=" + id + ", text=" + text + ", username=" + username + ", like=" + like + "]";
	}
}

