#!/usr/bin/env bash
#
# Run a program supposed to solve the 15-puzzle in a batch mode using various
# strategies for searching the state space and applying them to all the initial
# states of the puzzle stored in files in the current directory.
#
# The names of the files storing the initial states of the puzzle should obey
# the following format:
#  size_depth_id.txt
# for example:
#  4x4_01_00001.txt
#
# Optional arguments:
#  --strategy STRATEGY
#     use only strategy STRATEGY for searching the state space.
#  --param PARAM
#     use only the particular order specified as PARAM for blind strategies or
#     the particular heuristic specified as PARAM for informed strategies.
#
# TODO: Change variable $progcmd to match the command needed to invoke the
# actual program, using the absolute (or relative) path, for example:
#  progcmd='/home/user/15puzzle/bin/solver' (native code)
#  progcmd='java -jar /home/user/15puzzle/bin/solver.jar' (executable JAR file)
#  progcmd='python /home/user/15puzzle/bin/solver.py' (Python file)

progcmd='java -jar 15Puzzle.jar'
orders=(RDUL RDLU DRUL DRLU LUDR LURD ULDR ULRD)
heuristics=(hamm manh own)
init_filename_regex='^[a-zA-Z0-9]+_[0-9]+_[0-9]+.txt$'

usage() {
    echo "Usage: $scriptname [--strategy STRATEGY [--param PARAMETER]]"
}

runprog()
{
    for filename in *; do
        if [[ -f "$filename" && "$filename" =~ $init_filename_regex ]]; then
            filename_root="${filename%.txt}_${1}_${2,,}"
            sol_filename="${filename_root}_sol.txt"
            stats_filename="${filename_root}_stats.txt"
            $progcmd "$1" "$2" "$filename" "$sol_filename" "$stats_filename"
        fi
    done
}

runbfs()
{
    echo '===> Strategy: bfs <==='
    if [[ -n "$1" ]]; then
        echo " -> Order: $1"
        runprog bfs "$1"
    else
        for o in "${orders[@]}"; do
            echo " -> Order: $o"
            runprog bfs "$o"
        done
    fi
}

rundfs()
{
    echo '===> Strategy: dfs <==='
    if [[ -n "$1" ]]; then
        echo " -> Order: $1"
        runprog dfs "$1"
    else
        for o in "${orders[@]}"; do
            echo " -> Order: $o"
            runprog dfs "$o"
        done
    fi
}

runastr()
{
    echo '===> Strategy: astr <==='
    if [[ -n "$1" ]]; then
        echo " -> Heuristic: $1"
        runprog astr "$1"
    else
        for h in "${heuristics[@]}"; do
            echo " -> Heuristic: $h"
            runprog astr "$h"
        done
    fi
}

runall()
{
    runbfs
    rundfs
    runastr
}

# Parse arguments
scriptname=$(basename "$0")
for arg in "$@"; do
    if [[ "$arg" == '--help' ]]; then
        usage
        exit 0
    fi
done
while [[ "$#" > 0 ]]; do
    case "$1" in
        --param)
            if [[ ! "$2" ]] || [[ "$2" == '--strategy' ]]; then
                echo "$scriptname: error: missing parameter" >&2
                usage
                exit 2
            else
                param="$2"
            fi
            ;;
        --strategy)
            if [[ ! "$2" ]] || [[ "$2" == '--param' ]]; then
                echo "$scriptname: error: missing strategy" >&2
                usage
                exit 2
            else
                strategy="$2"
            fi
            ;;
        *)
            echo "$scriptname: error: unrecognized argument: $1" >&2
            usage
            exit 2
            ;;
    esac
    shift
    shift
done
if [[ -z "$strategy" ]] && [[ -n "$param" ]]; then
    echo "$scriptname: error: argument --param: requires argument" \
         "--strategy" >&2
    usage
    exit 2
fi

# Run a program solving the 15-puzzle using appropriate strategy/strategies
if [[ -z "$strategy" ]]; then
    runall
else
    case "$strategy" in
        bfs)
            runbfs "$param"
            ;;
        dfs)
            rundfs "$param"
            ;;
        astr)
            runastr "$param"
            ;;
        *)
            echo "$scriptname: error: unrecognized strategy: $strategy" >&2
            exit 1
            ;;
    esac
fi
