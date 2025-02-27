docker build -t kittenglm-app:latest .


docker run -d --name kittenChat-app -p 8091:8091 kittenGLM-app