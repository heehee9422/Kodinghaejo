function verify(num1, num2) {
    let answer=0;
    if(-50000<=num1 && num1<=50000 && -50000<=num2 && num2<=50000) {
        answer = num1-num2;
    }
    return answer;
}

module.exports = verify;