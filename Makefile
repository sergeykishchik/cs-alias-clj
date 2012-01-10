all: src/cs_alias_clj/core.clj project.clj make-cards.sh
	./make-cards.sh

clean:
	lein clean
	rm -f alias.* *.tar.gz

dist: src/cs_alias_clj/core.clj project.clj make-cards.sh
	lein deps
	lein uberjar
	tar cf cs-alias-clj-0.1.tar \
cs-alias-clj-1.0.0-SNAPSHOT-standalone.jar \
make-cards.sh words.txt people.txt
	lein clean
	gzip -9 cs-alias-clj-0.1.tar

.PHONY: clean dist
