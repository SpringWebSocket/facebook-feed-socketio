
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
		return feeds.add(feed);
	}

	@Override
	public boolean update(Feed feed) {
		for (Feed f : feeds) {
			if (f.getId() == feed.getId()) {
				f.setText(feed.getText());
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean remove(int id) {
		for (Feed f : feeds) {
			if (f.getId() == id) {
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
	public Feed findOne(int id) {
		for (Feed feed : feeds) {
			if (feed.getId() == id) {
				return feed;
			}
		}
		return null;
	}
}
