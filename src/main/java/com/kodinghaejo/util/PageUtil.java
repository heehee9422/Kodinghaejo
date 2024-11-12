package com.kodinghaejo.util;

public class PageUtil {

	//화면에 하나의 페이징만 필요할 때 사용
	public String getPageList(String src, int pageNum, int postNum, int pageListCount, int totalCount, String keyword) {
		//pageNum : 현재 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지리스트에 보여질 페이지 갯수
		//totalCount : 전체 행갯수
		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수
		//예) totalCount = 7, postNum = 5, totalPage = 2, totalSection = 1, pageListCount = 5

		int totalPage = (int) Math.ceil(totalCount / (double) postNum);
		int totalSection = (int) Math.ceil(totalPage / (double) pageListCount);
		int section = (int) Math.ceil(pageNum / (double) pageListCount);
		String pageList = "";

		if (totalPage > 1) {
			pageList += "<div class=\"pagenate mt20\">\n";
			
			for (int i = 1; i <= pageListCount; i++) {
				//1. <<, < 출력
				// - i == 1
				if (i == 1) {
					int prevPage = (section > 1) ? ((section - 2) * pageListCount + pageListCount) : 1;
					
					//<<(첫 페이지로 이동) 출력
					pageList += "\t<a href=\"" + src + "?page=1&keyword=" + keyword + "\" class=\"left-prev-arrow\">\n"
										+ "\t\t<svg width=\"12\" height=\"12\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
										+ "\t\t\t<path d=\"M11 1L1 11.1579L10.6891 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t\t<path d=\"M21 1L11 11.1579L20.6891 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t</svg>\n"
										+ "\t</a>\n";

					//<(이전 페이지로 이동) 출력
					pageList += "\t<a href=\"" + src + "?page=" + Integer.toString(prevPage) + "&keyword=" + keyword + "\" class=\"left-arrow\"><i class=\"xi-angle-left-thin\"></i></a>\n";
				}
				//2. 페이지 출력 중단
				if (totalPage < (section - 1) * pageListCount + i) { break; }
				//3. 인자로 가져 온 페이지 값과 계산해서 나온 페이지 값이 같으면 해당 페이지 활성상태 표시, 링크는 무조건 붙임.
				if (pageNum != (section - 1) * pageListCount + i)
					pageList += "\t<a href=\"" + src + "?page=" + Integer.toString((section - 1) * pageListCount + i) + "&keyword=" + keyword + "\">" + Integer.toString((section - 1) * pageListCount + i) + "</a>\n";
				else
					pageList += "\t<a href=\"" + src + "?page=" + Integer.toString((section - 1) * pageListCount + i) + "&keyword=" + keyword + "\" class=\"on\" role=\"button\">" + Integer.toString((section - 1) * pageListCount + i) + "</a>\n";
				//4. >, >> 출력
				// totalPage >= i + (section - 1) * pageListCount + 1 --> 아직까지 출력할 페이지가 남아 있음.
				if (i == pageListCount) {
					// && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1
					int nextPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? (section * pageListCount + 1) : totalPage;
					int lastPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? (totalSection * pageListCount) : totalPage;
					
					//>(다음 페이지로 이동) 출력
					pageList += "\t<a href=\"" + src + "?page=" + Integer.toString(nextPage) + "&keyword=" + keyword + "\" class=\"right-arrow\"><i class=\"xi-angle-right-thin\"></i></a>\n";
					
					//>>(마지막 페이지로 이동) 출력
					pageList += "\t<a href=\"" + src + "?page=" + Integer.toString(lastPage) + "&keyword=" + keyword + "\" class=\"right-next-arrow\">\n"
										+ "\t\t<svg width=\"12\" height=\"12\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
										+ "\t\t\t<path d=\"M11 1L21 11.1579L11.3109 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t\t<path d=\"M1 1L11 11.1579L1.31088 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t</svg>\n"
										+ "\t</a>\n";
				}
					
			}

			pageList += "</div>";
		}

		return pageList;
	}

	//MyPage - 내 게시판 페이징(게시글, 댓글 각각 적용)
	public String getMypageMyboardPageList(String type, int boardPageNum, int replyPageNum, 
			int postNum, int pageListCount, int boardTotalCount, int replyTotalCount) {
		//boardPageNum : 현재 게시글 페이지 번호
		//replyPageNum : 현재 댓글 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지리스트에 보여질 페이지 갯수
		//totalCount : 전체 행갯수
		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수
		//예) totalCount = 7, postNum = 5, totalPage = 2, totalSection = 1, pageListCount = 5
		int totalPage, totalSection, section;
		int page, subPage;
		String pageName, subPageName;
		
		if (type.equals("board")) {
			totalPage = (int) Math.ceil(boardTotalCount / (double) postNum);
			totalSection = (int) Math.ceil(totalPage / (double) pageListCount);
			section = (int) Math.ceil(boardPageNum / (double) pageListCount);
			page = boardPageNum;
			subPage = replyPageNum;
			pageName = "boardPage";
			subPageName = "replyPage";
		} else if (type.equals("reply")) {
			totalPage = (int) Math.ceil(replyTotalCount / (double) postNum);
			totalSection = (int) Math.ceil(totalPage / (double) pageListCount);
			section = (int) Math.ceil(replyPageNum / (double) pageListCount);
			page = replyPageNum;
			subPage = boardPageNum;
			pageName = "replyPage";
			subPageName = "boardPage";
		} else {
			return "";
		}
		
		String pageList = "";

		if (totalPage > 1) {
			pageList += "<div class=\"pagenate mt20\">\n";
			
			for (int i = 1; i <= pageListCount; i++) {
				//1. <<, < 출력
				// - i == 1
				if (i == 1) {
					int prevPage = (section > 1) ? ((section - 2) * pageListCount + pageListCount) : 1;
					
					//<<(첫 페이지로 이동) 출력
					pageList += "\t<a href=\"/member/mypage/myboard?" + pageName + "=1&" + subPageName + "=" + subPage + "\" class=\"left-prev-arrow\">\n"
										+ "\t\t<svg width=\"12\" height=\"12\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
										+ "\t\t\t<path d=\"M11 1L1 11.1579L10.6891 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t\t<path d=\"M21 1L11 11.1579L20.6891 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t</svg>\n"
										+ "\t</a>\n";

					//<(이전 페이지로 이동) 출력
					pageList += "\t<a href=\"/member/mypage/myboard?" + pageName + "=" + Integer.toString(prevPage) + "&" + subPageName + "=" + subPage + "\" class=\"left-arrow\"><i class=\"xi-angle-left-thin\"></i></a>\n";
				}
				//2. 페이지 출력 중단
				if (totalPage < (section - 1) * pageListCount + i) { break; }
				//3. 인자로 가져 온 페이지 값과 계산해서 나온 페이지 값이 같으면 해당 페이지 활성상태 표시, 링크는 무조건 붙임.
				if (page != (section - 1) * pageListCount + i)
					pageList += "\t<a href=\"/member/mypage/myboard?" + pageName + "=" + Integer.toString((section - 1) * pageListCount + i) + "&" + subPageName + "=" + subPage + "\">" + Integer.toString((section - 1) * pageListCount + i) + "</a>\n";
				else
					pageList += "\t<a href=\"/member/mypage/myboard?" + pageName + "=" + Integer.toString((section - 1) * pageListCount + i) + "&" + subPageName + "=" + subPage + "\" class=\"on\" role=\"button\">" + Integer.toString((section - 1) * pageListCount + i) + "</a>\n";
				//4. >, >> 출력
				// totalPage >= i + (section - 1) * pageListCount + 1 --> 아직까지 출력할 페이지가 남아 있음.
				if (i == pageListCount) {
					// && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1
					int nextPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? (section * pageListCount + 1) : totalPage;
					int lastPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? (totalSection * pageListCount) : totalPage;
					
					//>(다음 페이지로 이동) 출력
					pageList += "\t<a href=\"/member/mypage/myboard?" + pageName + "=" + Integer.toString(nextPage) + "&" + subPageName + "=" + subPage + "\" class=\"right-arrow\"><i class=\"xi-angle-right-thin\"></i></a>\n";
					
					//>>(마지막 페이지로 이동) 출력
					pageList += "\t<a href=\"/member/mypage/myboard?" + pageName + "=" + Integer.toString(lastPage) + "&" + subPageName + "=" + subPage + "\" class=\"right-next-arrow\">\n"
										+ "\t\t<svg width=\"12\" height=\"12\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
										+ "\t\t\t<path d=\"M11 1L21 11.1579L11.3109 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t\t<path d=\"M1 1L11 11.1579L1.31088 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t</svg>\n"
										+ "\t</a>\n";
				}
					
			}

			pageList += "</div>";
		}

		return pageList;
	}

}