FROM node:8
ADD ./ /home/node
WORKDIR /home/node
RUN npm run-script build
RUN npm install
