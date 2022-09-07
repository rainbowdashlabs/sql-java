FROM python:3.10

RUN pip install mkdocs-material && pip install mkdocs-git-revision-date-localized-plugin

EXPOSE 8000

WORKDIR "/docs"

COPY mkdocs.yml /docs/mkdocs.yml

COPY docs/ /docs/docs

COPY .git/ /docs/.git/

ENTRYPOINT ["mkdocs", "serve"]
