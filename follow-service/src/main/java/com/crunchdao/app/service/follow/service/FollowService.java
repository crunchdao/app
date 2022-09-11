package com.crunchdao.app.service.follow.service;

import java.util.Objects;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crunchdao.app.common.web.model.PageResponse;
import com.crunchdao.app.service.follow.dto.FollowDto;
import com.crunchdao.app.service.follow.dto.StatisticsDto;
import com.crunchdao.app.service.follow.entity.Follow;
import com.crunchdao.app.service.follow.exception.AlreadyFollowingException;
import com.crunchdao.app.service.follow.exception.FollowingYourselfException;
import com.crunchdao.app.service.follow.exception.NotFollowingException;
import com.crunchdao.app.service.follow.repository.FollowRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class FollowService {
	
	private final FollowRepository repository;
	
	public PageResponse<FollowDto> listFollowers(UUID peerId, Pageable pageable) {
		final Page<Follow> page = repository.findAllByIdPeerId(peerId, pageable);
		
		return new PageResponse<>(page, Follow::toFollowerDto);
	}
	
	public PageResponse<FollowDto> listFollowing(UUID peerId, Pageable pageable) {
		final Page<Follow> page = repository.findAllByIdUserId(peerId, pageable);
		
		return new PageResponse<>(page, Follow::toFollowingDto);
	}
	
	public Follow follow(UUID userId, UUID peerId) {
		if (Objects.equals(userId, peerId)) {
			throw new FollowingYourselfException();
		}
		
		return insert(new Follow()
			.setId(userId, peerId));
	}
	
	@Transactional
	public void unfollow(UUID userId, UUID peerId) {
		long deleted = repository.removeById(Follow.id(userId, peerId));
		
		if (deleted == 0l) {
			throw new NotFollowingException(peerId);
		}
	}
	
	public StatisticsDto getStatistics(UUID userId, UUID authenticatedUserId) {
		long followers = repository.countByIdPeerId(userId);
		long followings = repository.countByIdUserId(userId);
		Boolean followed = isFollowing(authenticatedUserId, userId);
		
		return new StatisticsDto(userId, followers, followings, followed);
	}
	
	public Boolean isFollowing(UUID userId, UUID peerId) {
		if (userId == null) {
			return null;
		}
		
		return repository.existsById(Follow.id(userId, peerId));
	}
	
	/* for some reason, hibernate does not throw the right exception when the duplicate is for the composite ID */
	private Follow insert(Follow follow) {
		try {
			return repository.saveAndFlush(follow);
		} catch (DataIntegrityViolationException exception) {
			throw new AlreadyFollowingException(follow.getId().getPeerId(), exception);
		}
	}
	
}