package com.kodinghaejo.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.kodinghaejo.dto.BoardDTO;
import com.kodinghaejo.dto.ReplyDTO;
import com.kodinghaejo.dto.ReplyInterface;
import com.kodinghaejo.entity.BoardEntity;
import com.kodinghaejo.entity.BoardRecommendEntity;
import com.kodinghaejo.entity.BoardRecommendEntityId;
import com.kodinghaejo.entity.MemberEntity;
import com.kodinghaejo.entity.ReplyEntity;
import com.kodinghaejo.entity.repository.BoardRecommendRepository;
import com.kodinghaejo.entity.repository.BoardRepository;
import com.kodinghaejo.entity.repository.FileRepository;
import com.kodinghaejo.entity.repository.MemberRepository;
import com.kodinghaejo.entity.repository.ReplyRepository;
import com.kodinghaejo.entity.repository.TestQuestionAnswerRepository;
import com.kodinghaejo.entity.repository.TestQuestionRepository;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {
	
	private final BoardRepository boardRepository;
	private final BoardRecommendRepository boardRecommendRepository;
	private final FileRepository fileRepository;
	private final MemberRepository memberRepository;
	private final ReplyRepository replyRepository;
	private final TestQuestionRepository testQuestionRepository;
	private final TestQuestionAnswerRepository testQuestionAnswerRepository;

	//게시물 등록하기
	@Override
	public void write(BoardDTO board) throws Exception {
		board.setRegdate(LocalDateTime.now());
		board.setHitCnt(0);
		boardRepository.save(board.dtoToEntity(board));	
	}
	
	//게시물 리스트보기
	@Override
	public List<BoardEntity> getPosts(){
		return boardRepository.findByIsUseOrderByRegdateDesc("Y");
	}
	
	//게시물 내용 보기
	@Override
	public BoardDTO view(Long idx) throws Exception {
		return boardRepository.findById(idx).map(view -> new BoardDTO(view)).get();
	}
	
	//게시물 수정하기
	@Override
	public void modify(BoardDTO board)throws Exception{
		BoardEntity boardEntity = boardRepository.findById(board.getIdx()).get();
		boardEntity.setTitle(board.getTitle());
		boardEntity.setContent(board.getContent());
		boardEntity.setCat(board.getCat());
		boardRepository.save(boardEntity);
		
	}
	
	//게시물 비활성화 (isUse y->n)
	@Override
	public void deactivePost(BoardDTO board)throws Exception {
		BoardEntity boardEntity = boardRepository.findById(board.getIdx()).get();
		boardEntity.setIsUse("N"); // isUse 값을 N으로 설정하여 비활성화
		boardRepository.save(boardEntity);
	}

	//댓글 목록보기
	public List<ReplyEntity>replyView(Long prntIdx){
		return replyRepository.findByPrntIdx(prntIdx);
	}

	//댓글 등록
	@Override
	public void replyWrite(ReplyDTO reply)throws Exception{
		reply.setRegdate(LocalDateTime.now());
		replyRepository.save(reply.dtoToEntity(reply));
	}
			
	// 댓글 삭제 (isUse를 "N"으로 설정)
	@Override
	public void replyDeactive(ReplyInterface reply) {
		ReplyEntity replyEntity = replyRepository.findById(reply.getIdx()).get();
		replyEntity.setIsUse("N");
		replyRepository.save(replyEntity);
	}
	
	//댓글 수정
	@Override
	public void replyModify(ReplyInterface reply){
		ReplyEntity replyEntity = replyRepository.findById(reply.getIdx()).get();
		replyEntity.setContent(reply.getContent());	
		replyRepository.save(replyEntity);
	}

	//댓글수 확인
	@Override
	public int getReplyCountByPostId(Long prntIdx) {
		return replyRepository.countRepliesByPostId(prntIdx);
	}
	
	// 다수의 게시물 ID에 대한 댓글 수를 조회하여 Map으로 반환
	@Override
	public Map<Long, Integer> getReplyCounts(List<Long> prntIdx) {
		if (prntIdx.isEmpty()) {
			return Collections.emptyMap();
		}
		return prntIdx.stream()
									.collect(Collectors.toMap(postId -> postId, postId -> replyRepository.countRepliesByPostId(postId)));
	}
	
	//게시물 내용 조회수 증가
	@Override
	public void hitno(Long seqno) throws Exception {
		boardRepository.hitno(seqno);
	}
	
	
	// 개별 게시물의 좋아요 수를 반환하는 메서드
	@Override
	public long getLikeCount(Long bidx){
		
		long likeCount =  boardRecommendRepository.countByBoardIdx(bidx);
		return likeCount > 0 ? likeCount : 0;
	}

	// 모든 게시물의 좋아요 수를 조회하는 메서드
	@Override
	public Map<Long, Long> getAllBoardLikeCounts(List<Long> postIds) {
		Map<Long, Long> likeCounts = new HashMap<>();

		for (Long postId : postIds) {
			// 각 게시물 ID에 대해 좋아요 수 조회
			long likeCount = boardRecommendRepository.countByBoardIdx(postId);
			likeCounts.put(postId, likeCount);
		}

		return likeCounts;
	}
	
	//좋아요 상태 확인
	@Override
	public String isPostLikedByUser(String email, Long boardIdx) {
		int count = boardRecommendRepository.countByEmailAndBoardIdx(email, boardIdx);
		return count > 0 ? "yes" : "no";
	}

	@Transactional
	public boolean likeUp(String email, Long postIdx) {
		BoardRecommendEntityId id = new BoardRecommendEntityId(email, postIdx);
		
		MemberEntity member = memberRepository.findById(email)
														.orElseThrow(() -> new IllegalArgumentException("해당 이메일을 가진 사용자가 없습니다: " + email));
		BoardEntity board = boardRepository.findById(postIdx)
													.orElseThrow(() -> new IllegalArgumentException("해당 게시물 ID가 없습니다: " + postIdx));
		
		if (boardRecommendRepository.existsById(id)) {
			return false;
		}
		BoardRecommendEntity recommend = new BoardRecommendEntity();
		recommend.setEmail(member);
		recommend.setBoardIdx(board);
		recommend.setGoodChk("Y");
		recommend.setGoodDate(LocalDateTime.now());
		boardRecommendRepository.save(recommend);
		return true;
	}

	@Transactional
	public boolean likeDown(String email, Long postIdx) {
		BoardRecommendEntityId id = new BoardRecommendEntityId(email, postIdx);
		if (!boardRecommendRepository.existsById(id)) {
			return false;
		}
		boardRecommendRepository.deleteById(id);
		return false;
	}
	
	//신고하기
	@Transactional
	@Override
	public String reportPost(String email, Long boardIdx) {
		BoardRecommendEntityId id = new BoardRecommendEntityId(email, boardIdx);
		BoardRecommendEntity recommend = boardRecommendRepository.findById(id).orElse(null);

		if (recommend == null) {
			// 새로운 신고 등록
			recommend = new BoardRecommendEntity();
			recommend.setEmail(memberRepository.findById(email).orElseThrow());
			recommend.setBoardIdx(boardRepository.findById(boardIdx).orElseThrow());
			recommend.setBadChk("Y");
			recommend.setBadDate(LocalDateTime.now());
			boardRecommendRepository.save(recommend);
			return "reported";
		} else if (!"Y".equals(recommend.getBadChk())) {
			// 기존 좋아요에 대해 신고 상태만 업데이트
			recommend.setBadChk("Y");
			recommend.setBadDate(LocalDateTime.now());
			boardRecommendRepository.save(recommend);
			return "reported";
		} else {
			// 이미 신고된 경우
			return "already_reported";
		}
	}
	
	//공지사항 화면
	@Override
	public List<BoardDTO> getAllNotices() {
		List<BoardEntity> boardEntities = boardRepository.findByCat("공지사항");
		List<BoardDTO> boardDTOs = new ArrayList<>();
		
		for (BoardEntity board : boardEntities) {
			BoardDTO boardDTO = new BoardDTO(board);
			boardDTOs.add(boardDTO);
		}
		
		return boardDTOs;
	}
	
	//내가 작성한 게시글(마이 페이지)
	@Override
	public Page<BoardDTO> mypageBoardList(String email, int pageNum, int postNum) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<BoardEntity> boardEntities = boardRepository.findByEmailAndIsUse(memberEntity, "Y", pageRequest);
		List<BoardDTO> boardDTOs = new ArrayList<>();
		
		for (BoardEntity boardEntity : boardEntities) {
			BoardDTO board = new BoardDTO(boardEntity);
			board.setGoodCnt(boardRecommendRepository.countByBoardIdxAndGoodChk(boardEntity, "Y"));
			boardDTOs.add(board);
		}
		
		return new PageImpl<>(boardDTOs, pageRequest, boardEntities.getTotalElements());
	}
	
	//내가 작성한 댓글(마이 페이지)
	@Override
	public Page<ReplyDTO> mypageReplyList(String email, int pageNum, int postNum) {
		MemberEntity memberEntity = memberRepository.findById(email).get();
		
		PageRequest pageRequest = PageRequest.of(pageNum - 1, postNum, Sort.by(Direction.DESC, "idx"));
		Page<ReplyEntity> replyEntities = replyRepository.findByEmailAndIsUse(memberEntity, "Y", pageRequest);
		List<ReplyDTO> replyDTOs = new ArrayList<>();
		
		for (ReplyEntity replyEntity : replyEntities) {
			ReplyDTO reply = new ReplyDTO(replyEntity);
			String prntTitle = (replyEntity.getRePrnt().equals("QA")) ? testQuestionAnswerRepository.findById(replyEntity.getPrntIdx()).get().getContent() :
													(replyEntity.getRePrnt().equals("Q")) ? testQuestionRepository.findById(replyEntity.getPrntIdx()).get().getTitle() :
													(replyEntity.getRePrnt().equals("FR")) ? boardRepository.findById(replyEntity.getPrntIdx()).get().getTitle() : "";
			reply.setPrntTitle(prntTitle);
			replyDTOs.add(reply);
		}
		
		return new PageImpl<>(replyDTOs, pageRequest, replyEntities.getTotalElements());
	}

}