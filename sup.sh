#! /bin/bash
# Starts the application by default as server and on port 9200
# defined in the sup.yml file.

set -e

readonly deploy=build/install/sup/bin
readonly sup_stdout=$deploy/sup.out
readonly sup_stderr=$deploy/sup.err

cd "$(dirname "${BASH_SOURCE[0]}")"

exec $deploy/sup server src/dist/conf/sup.yml \
            1>$sup_stdout 2>$sup_stderr