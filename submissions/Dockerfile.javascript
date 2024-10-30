FROM node:14
WORKDIR /app
COPY Solution.js .
CMD ["node", "Solution.js"]