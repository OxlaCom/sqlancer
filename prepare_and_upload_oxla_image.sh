#!/bin/bash

set -xe

source ~/.oxla/compile

docker build -t ${SQLANCER_IMAGE_TAG} -f oxla.Dockerfile .

docker push ${SQLANCER_IMAGE_TAG}
