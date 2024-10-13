docker run
--name Nginx
-p 443:443 -p 80:80
-v /nginx/logs:/var/log/nginx
-v /nginx/html:/usr/share/nginx/html
-v /nginx/conf/nginx.conf:/etc/nginx/nginx.conf
-v /nginx/conf/conf.d:/etc/nginx/conf.d
-v /nginx/ssl:/etc/nginx/ssl/  
--privileged=true -d --restart=always nginx