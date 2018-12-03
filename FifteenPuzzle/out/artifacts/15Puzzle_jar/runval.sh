#!/usr/bin/env bash
#
# Run a program validating solutions to the 15-puzzle stored in files in the
# current directory against corresponding initial states of the puzzle also
# stored in files in the current directory in a batch mode.
#
# The names of the files storing the initial states of the puzzle should obey
# the following format:
#  size_depth_id.txt
# for example:
#  4x4_01_00001.txt
#
# The names of the files storing the solutions to the puzzle should obey the
# following format:
#  size_depth_id_strategy_param_sol.txt
# for example:
#  4x4_01_00001_bfs_rdul_sol.txt
#
# TODO: Change variable $progcmd to match the command needed to invoke the
# actual program, using the absolute (or relative) path, for example:
#  progcmd='java -jar /home/user/15puzzle/bin/puzzleval.jar'

progcmd='echo program'
sol_filename_regex='^([a-zA-Z0-9]+_[0-9]+_[0-9]+)_[a-zA-Z]+_[a-zA-Z]+_sol.txt$'

n_correct_sols=0
n_incorrect_sols=0
incorrect_sol_filenames=()
for filename in *; do
    if [[ -f "$filename" && "$filename" =~ $sol_filename_regex ]]; then
        init_filename="${BASH_REMATCH[1]}.txt"
        echo -n "$filename: "
        $progcmd "$init_filename" "$filename"
        if [[ $? == 0 ]]; then
            n_correct_sols=$((n_correct_sols + 1))
        elif [[ $? == 1 ]]; then
            n_incorrect_sols=$((n_incorrect_sols + 1))
            incorrect_sol_filenames+=("$filename")
        else
            scriptname=$(basename "$0")
            echo "$scriptname: fatal error" >&2
            exit 1
        fi
    fi
done

echo "----- Summary -----"
echo "Correct solutions: $n_correct_sols"
echo "Incorrect solutions: $n_incorrect_sols"
for filename in "${incorrect_sol_filenames[@]}"; do
    echo "$filename"
done
