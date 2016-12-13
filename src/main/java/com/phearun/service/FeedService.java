
package com.phearun.service;

import com.phearun.model.Feed;

import java.util.List;

/**
 * Created by PHEARUN on 12/9/2016.
 */

public interface FeedService {

	boolean save(Feed feed);

	boolean update(Feed feed);

	boolean remove(String id);

	List<Feed> findAll();

	Feed findOne(String id);
	
	int updateLike(String id);

}
