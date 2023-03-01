FROM python:3.11

RUN pip install mkdocs-material && pip install mkdocs-git-revision-date-localized-plugin

EXPOSE 8000

WORKDIR "/docs"

COPY config/mkdocs.yml mkdocs.yml

COPY docs/ docs/

COPY .git/ .git/

# We replace all db specific markdowns with sql again, since mkdocs can't render those.
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```mariadb/```sql/g'
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```mysql/```sql/g'
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```sqlite/```sql/g'
RUN find docs/ -type f -print0 | xargs -0 sed -i 's/```postgresql/```sql/g'

ENTRYPOINT ["mkdocs", "serve", "-a", "0.0.0.0:80"]
