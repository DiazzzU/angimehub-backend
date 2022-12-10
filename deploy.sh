sudo apt update
sudo apt install docker.io -y
sudo apt install docker-compose -y
pkill -f 'java -jar'
sudo docker login -u $CI_DEPLOY_USER -p $CI_DEPLOY_PASSWORD registry.gitlab.com
sudo docker system prune --all -f
sudo docker-compose down
sudo docker-compose pull
sudo docker-compose up -d
