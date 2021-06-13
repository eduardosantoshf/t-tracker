#FROM nginx:alpine
FROM nginx:latest
RUN rm -rf /usr/share/nginx/html
RUN rm -rf /etc/share/nginx/html
COPY . /usr/share/nginx/html
COPY . /etc/share/nginx/html
RUN chmod +r /usr/share/nginx/html/index.html
