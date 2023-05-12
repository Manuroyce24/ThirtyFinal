package org.thirty.app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.thirty.app.model.BlogUser;
import org.thirty.app.model.FriendRequest;
import org.thirty.app.model.FriendRequest.FriendRequestStatus;

@Repository
public interface FriendRequestRepository extends JpaRepository<FriendRequest, Long> {
	List<FriendRequest> findByReceiverAndStatus(BlogUser receiver, FriendRequest.FriendRequestStatus status);

	Optional<FriendRequest> findBySenderAndReceiverAndStatus(BlogUser sender, BlogUser receiver, FriendRequestStatus status);


}
