FROM node:22-alpine AS build
LABEL authors="shdwd"

WORKDIR /app

COPY citabella-web/package*.json ./
RUN npm install

COPY citabella-web/ .

RUN npm run build -- --configuration production

FROM nginx:alpine

COPY --from=build /app/dist/*/browser /usr/share/nginx/html

EXPOSE 80