
package com.phearun.repository;

import java.util.List;

import com.phearun.model.Feed;

/**
 * Created by PHEARUN on 12/9/2016.
 */

public interface FeedRepository {

	boolean save(Feed feed);

	boolean update(Feed feed);

	boolean remove(int id);

	List<Feed> findAll();

	Feed findOne(int id);
}
