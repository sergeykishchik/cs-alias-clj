all: src/cs_alias_clj/core.clj project.clj make-cards.sh
	./make-cards.sh

clean:
	lein clean
	rm -f alias.*

.PHONY: clean
