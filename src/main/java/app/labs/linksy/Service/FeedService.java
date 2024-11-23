package app.labs.linksy.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.labs.linksy.DAO.FeedRepository;
import app.labs.linksy.Model.Feed;

@Service
public class FeedService implements IFeedService {
	
	@Autowired
	private FeedRepository feedRepository;
	
	// 피드 데이터 가져오기
    public List<Feed> getFeedsWithDetails() {
        return feedRepository.getFeedsWithDetails();
    }
}
