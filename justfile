# Shows available recipes
help:
    just --list

# Run the main application
run:
    clojure -M -m example.main

# Start a REPL with nREPL and CIDER support
nrepl:
    clojure -M:nrepl --extra-paths dev
