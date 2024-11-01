function getRandomInt(min, max) {
    return Math.floor(Math.random() * (max - min + 1)) + min;
}
let num1 = getRandomInt(1, 10);
let num2 = getRandomInt(1, 10);

const solution = require('./Solution');
const verify = require('./Verify');

const result_s = solution(num1,num2);
const result_v = verify(num1,num2);
console.log("Test: " + (result_s == result_v ? "Pass" : "Fail"));