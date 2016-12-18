
package com.phearun.repository.impl;

import com.phearun.model.Feed;
import com.phearun.repository.FeedRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by PHEARUN on 12/9/2016.
 */

@Repository
public class FeedRepositoryImpl implements FeedRepository {

	private List<Feed> feeds = new ArrayList<>();

	@Override
	public boolean save(Feed feed) {
		feeds.add(0, feed);
		//return feeds.add(feed);
		return true;
	}

	@Override
	public boolean update(Feed feed) {
		for (Feed f : feeds) {
			if (f.getId().equals(feed.getId())) {
				f.setText(feed.getText());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(String id) {
		for (Feed f : feeds) {
			if (f.getId().equals(id)) {
				return feeds.remove(f);
			}
		}
		return false;
	}

	@Override
	public List<Feed> findAll() {
		return feeds;
	}

	@Override
	public Feed findOne(String id) {
		for (Feed feed : feeds) {
			if (feed.getId().equals(id)) {
				return feed;
			}
		}
		return null;
	}

	@Override
	public int updateLike(String id) {
		for (Feed feed : feeds) {
			if (feed.getId().equals(id)) {
				feed.setLike(feed.getLike() + 1);
				return feed.getLike();
			}
		}
		return 0;
	}
	
}
