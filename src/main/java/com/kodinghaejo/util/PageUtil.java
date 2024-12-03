package com.kodinghaejo.util;

public class PageUtil {

	public String getPageList(String target, String pageName, int pageNum, int postNum, int pageListCount, int totalCount, String params) {
		//target : 페이지 주소
		//pageName : 페이지 이름
		//pageNum : 현재 페이지 번호
		//postNum : 한 화면에 보여지는 게시물 행 갯수
		//pageListCount : 하단 페이지리스트에 보여질 페이지 갯수
		//totalCount : 전체 행 갯수
		//params : 페이지를 제외한 나머지 파라미터값

		//totalPage : 전체 페이지 갯수
		//section : 한 개의 페이지 목록 예) 1 2 3 4 5 --> section 1, 6 7 8 9 10 --> section 2
		//totalSection : 전체 section 갯수
		//예) totalCount = 7, postNum = 5, totalPage = 2, totalSection = 1, pageListCount = 5

		int totalPage = (int) Math.ceil(totalCount / (double) postNum);
		int totalSection = (int) Math.ceil(totalPage / (double) pageListCount);
		int section = (int) Math.ceil(pageNum / (double) pageListCount);
		String pageList = "";

		if (totalPage > 1) {
			pageList += "<div class=\"paginate mt20\">\n";

			for (int i = 1; i <= pageListCount; i++) {
				//1. <<, < 출력
				// - i == 1
				if (i == 1) {
					int prevPage = (section > 1) ? ((section - 2) * pageListCount + pageListCount) : 1;

					//<<(첫 페이지로 이동) 출력
					pageList += "\t<a href=\"" + target + "?" + pageName + "=1" + params + "\" class=\"left-prev-arrow\">\n"
										+ "\t\t<svg width=\"12\" height=\"12\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
										+ "\t\t\t<path d=\"M11 1L1 11.1579L10.6891 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t\t<path d=\"M21 1L11 11.1579L20.6891 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t</svg>\n"
										+ "\t</a>\n";

					//<(이전 페이지로 이동) 출력
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString(prevPage) + params + "\" class=\"left-arrow\"><i class=\"xi-angle-left-thin\"></i></a>\n";
				}
				//2. 페이지 출력 중단
				if (totalPage < (section - 1) * pageListCount + i) {
					int nextPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? (section * pageListCount + 1) : totalPage;
					int lastPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? ((totalSection - 1) * pageListCount + (totalPage % pageListCount)) : totalPage;

					//>(다음 페이지로 이동) 출력
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString(nextPage) + params + "\" class=\"right-arrow\"><i class=\"xi-angle-right-thin\"></i></a>\n";

					//>>(마지막 페이지로 이동) 출력
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString(lastPage) + params + "\" class=\"right-next-arrow\">\n"
										+ "\t\t<svg width=\"12\" height=\"12\" viewBox=\"0 0 22 22\" fill=\"none\" xmlns=\"http://www.w3.org/2000/svg\">\n"
										+ "\t\t\t<path d=\"M11 1L21 11.1579L11.3109 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t\t<path d=\"M1 1L11 11.1579L1.31088 21\" stroke=\"black\" stroke-linecap=\"round\"></path>\n"
										+ "\t\t</svg>\n"
										+ "\t</a>\n";
					break;
				}
				//3. 인자로 가져 온 페이지 값과 계산해서 나온 페이지 값이 같으면 해당 페이지 활성상태 표시, 링크는 무조건 붙임.
				if (pageNum != (section - 1) * pageListCount + i)
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString((section - 1) * pageListCount + i) + params + "\">" + Integer.toString((section - 1) * pageListCount + i) + "</a>\n";
				else
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString((section - 1) * pageListCount + i) + params + "\" class=\"on\" role=\"button\">" + Integer.toString((section - 1) * pageListCount + i) + "</a>\n";
				//4. >, >> 출력
				// totalPage >= i + (section - 1) * pageListCount + 1 --> 아직까지 출력할 페이지가 남아 있음.
				if (i == pageListCount) {
					// && totalSection > 1 && totalPage >= i + (section - 1) * pageListCount + 1
					int nextPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? (section * pageListCount + 1) : totalPage;
					int lastPage = (totalPage >= i + (section - 1) * pageListCount + 1) ? ((totalSection - 1) * pageListCount + (totalPage % pageListCount)) : totalPage;

					//>(다음 페이지로 이동) 출력
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString(nextPage) + params + "\" class=\"right-arrow\"><i class=\"xi-angle-right-thin\"></i></a>\n";

					//>>(마지막 페이지로 이동) 출력
					pageList += "\t<a href=\"" + target + "?" + pageName + "=" + Integer.toString(lastPage) + params + "\" class=\"right-next-arrow\">\n"
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