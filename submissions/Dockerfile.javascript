FROM node:14

WORKDIR /app

COPY Solution.js .
COPY Verify.js .
COPY Main.js .

CMD ["node", "Main.js"]