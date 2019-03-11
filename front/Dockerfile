FROM node:8
#ENV NPM_CONFIG_LOGLEVEL warn

ARG http_proxy="http://10.158.100.1:8080/"
ARG https_proxy="http://10.158.100.1:8080/"
RUN npm config set proxy && npm config set https-proxy "http://10.158.100.1:8080/"
ADD package.json package.json
RUN npm install
ADD . .
RUN npm run build --production
RUN npm install -g serve
#RUN serve -s build
EXPOSE 80
CMD ["serve", "-s","build","-l", "80"]