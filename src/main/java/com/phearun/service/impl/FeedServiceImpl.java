
package com.phearun.service.impl;

import com.phearun.model.Feed;
import com.phearun.repository.FeedRepository;
import com.phearun.service.FeedService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by PHEARUN on 12/9/2016.
 */

@Service
public class FeedServiceImpl implements FeedService {

	@Autowired
	private FeedRepository feedRepository;

	@Override
	public boolean save(Feed feed) {
		return feedRepository.save(feed);
	}

	@Override
	public boolean update(Feed feed) {
		return feedRepository.update(feed);
	}

	@Override
	public boolean remove(String id) {
		return feedRepository.remove(id);
	}

	@Override
	public List<Feed> findAll() {
		return feedRepository.findAll();
	}

	@Override
	public Feed findOne(String id) {
		return feedRepository.findOne(id);
	}
}
