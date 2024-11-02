# Shows available recipes
help:
    just --list --color=never

# Run the main application
start:
    clojure -M -m example.caveman-demo

# Start a REPL with nREPL and CIDER support
nrepl:
    if [ "$(uname -s)" = "MINGW" ]; then clojure -M:dev -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware]"; else rlwrap bb clojure -M:dev -m nrepl.cmdline --middleware "[cider.nrepl/cider-middleware]"; fi

# Check for code formatting issues in the src and dev directories
format_check:
    clojure -M:format -m cljfmt.main check src dev test

# Fix code formatting issues in the src and dev directories
format:
    clojure -M:format -m cljfmt.main fix src dev test

# Run linter clj-kondo to detect code issues
lint:
    clojure -M:lint -m clj-kondo.main --lint .

# Run tests with kaocha
test:
    clojure -M:dev -m kaocha.runner

# Check for outdated dependencies
outdated_check:
    clojure -Sdeps '{:deps {com.github.liquidz/antq {:mvn/version "RELEASE"}}}' -M -m antq.core

# Upgrade outdated versions interactively
outdated:
    clojure -Sdeps '{:deps {com.github.liquidz/antq {:mvn/version "RELEASE"}}}' -M -m antq.core --upgrade
