FROM python:3.13-bullseye AS base

COPY . .
RUN pip install mkdocs-material && pip install mkdocs-git-revision-date-localized-plugin

RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```mariadb/```sql/g'
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```mysql/```sql/g'
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```sqlite/```sql/g'
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```postgresql/```sql/g'

RUN mkdocs build -f config/de/mkdocs.yml
RUN mkdocs build -f config/en/mkdocs.yml
COPY docs/index.html site/index.html

FROM nginx:alpine

COPY --from=base /site /usr/share/nginx/html

EXPOSE 80
