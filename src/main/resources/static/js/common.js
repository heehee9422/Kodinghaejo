/* common-js */

//==================== Toast UI 에디터 값 처리 ====================
//탭 및 줄바꿈 처리 (에디터 -> DB 저장 시)
const setEditorContent = (content) => {
	return content.replaceAll('\t', '\\t').replaceAll('\n', '\\n');
}

//탭 및 줄바꿈 처리 (DB -> 에디터 및 뷰어 호출 시)
const getEditorContent = (content) => {
	return content.replaceAll('\\t', '\t').replaceAll('\\n', '\n');
}

//==================== 공통코드 관련 ====================

//유형과 코드로 값 찾기
const getCodeValue = (codeObj, type, code) => {
	let codeVal = '';
	
	switch (type) {
		case 'lvl':
			for (const i in codeObj.lvl) {
				if (codeObj.lvl[i].code == code)
					codeVal = codeObj.lvl[i].val;
			}
			break;
		
		case 'tec':
			for (const i in codeObj.tec) {
				if (codeObj.tec[i].code == code)
					codeVal = codeObj.tec[i].val;
			}
			break;
			
		case 'job':
			for (const i in codeObj.job) {
				if (codeObj.job[i].code == code)
					codeVal = codeObj.job[i].val;
			}
			break;
			
		case 'lng':
			for (const i in codeObj.lng) {
				if (codeObj.lng[i].code == code)
					codeVal = codeObj.lng[i].val;
			}
			break;
			
		case 'cat':
			for (const i in codeObj.cat) {
				if (codeObj.cat[i].code == code)
					codeVal = codeObj.cat[i].val;
			}
			break;
	}
	
	return codeVal;
}

//select에 코드 유형의 option 세팅 (코드 선택 포함)
const setSelectOption = (codeObj, target, type, code) => {
	switch (type) {
		case 'lvl':
			for (const i in codeObj.lvl) {
				let opt = document.createElement('option');
				opt.setAttribute('value', codeObj.lvl[i].code);
				opt.innerText = codeObj.lvl[i].val;
				if (codeObj.lvl[i].code == code) opt.setAttribute('selected', '');
				target.appendChild(opt);
			}
			break;
		
		case 'tec':
			for (const i in codeObj.tec) {
				let opt = document.createElement('option');
				opt.setAttribute('value', codeObj.tec[i].code);
				opt.innerText = codeObj.tec[i].val;
				if (codeObj.tec[i].code == code) opt.setAttribute('selected', '');
				target.appendChild(opt);
			}
			break;
			
		case 'job':
			for (const i in codeObj.job) {
				let opt = document.createElement('option');
				opt.setAttribute('value', codeObj.job[i].code);
				opt.innerText = codeObj.job[i].val;
				if (codeObj.job[i].code == code) opt.setAttribute('selected', '');
				target.appendChild(opt);
			}
			break;
			
		case 'lng':
			for (const i in codeObj.lng) {
				let opt = document.createElement('option');
				opt.setAttribute('value', codeObj.lng[i].code);
				opt.innerText = codeObj.lng[i].val;
				if (codeObj.lng[i].code == code) opt.setAttribute('selected', '');
				target.appendChild(opt);
			}
			break;
			
		case 'cat':
			for (const i in codeObj.cat) {
				let opt = document.createElement('option');
				opt.setAttribute('value', codeObj.cat[i].code);
				opt.innerText = codeObj.cat[i].val;
				if (codeObj.cat[i].code == code) opt.setAttribute('selected', '');
				
				if (codeObj.cat[i].code != 'CAT-0001')
					target.appendChild(opt);
			}
			break;
	}
}