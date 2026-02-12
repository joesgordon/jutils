# 
# 
# 
# 

echo "========================= Loading Bash Extras ========================="

user_rc_file="~/.beuserrc"

alias rs='source ~/.bashrc'
alias rse='exec bash'
alias ec='fe ~/.bashrc'
alias ecc='fe /apps/bashextras.sh'
alias ecu='fe $user_rc_file'
alias gou='go ~'
alias gof='go /files/'
alias h='history'

alias uls='ls -p' #append '/' to directories.
alias lsa='uls -a'
alias lsd='uls -d'
alias ll='uls -l'
alias lla='ll -a'
alias lld='ll -d'
alias geterr='errcode="$?"; if [[ $errcode -eq "0" ]] ; then echo "No Error"; else echo "ERROR $errcode"; fi;'

alias gitdi='git difftool -y'
alias gitcfg='git config -l --show-origin | sort'
alias gitci='git commit -m '
alias gitad='git add'
alias gitst='git status -s'
alias gitup='git pull -v --progress;git submodule update --recursive --remote'
alias gitlc='git config -l --show-origin'
alias gitwipe='git clean -f -d -X'

alias setcmd='sudo systemctl set-default multi-user'
alias setgui='sudo systemctl set-default graphical'

export HISTTIMEFORMAT="%04y-%02m-%02d %02H:%02M:%02S "
export PS1='\[\e]0;\w\a\]\n\[\e[32m\]\u@\h \[\e[33m\]\w\[\e[0m\]\n[\d \t]\$ '
#export PS1='\[\e]0;\w\a\]\n\u@\h \w\n[\d \t] $ '
export PS2='> '
export PS4='+ '

# export PATH=$PATH:/another/path/goes/here

function behelp()
{
    userrc_exists='does not exist'
    if [ -f "${user_rc_file}" ] ; then
        userrc_exists='exists'
    fi
cat << EOF
BashExtras Help

Your user resource file path is: $user_rc_file
This file ${userrc_exists}.

Functions:

 findcf - Finds all C/C++ extension files in the specified (or current if not 
          specified) directory or directories.
  findf - Looks is the directory (first argument) for a file matching the 
          pattern (second argument).
eclipse - Starts eclipse as a background task.
     fe - Starts your file editor (text editor) in the background.
    few - Looks for the single required argument in the user's path and if
          found, opens the result in your file editor.
     go - Changes to the provided directory (or home if blank) and lists the
          files there.
 behelp - Prints this help.
helpgos - Prints all aliases that start with "go".
     fm - Starts your file manager in the background.
     up - Changes directory to the number of parent directories provided or 1 
          if blank (e.g. "up 1" is the same as "up").

EOF
}

function eclipse()
{
    ( /usr/bin/eclipse $* 1>/dev/null 2>&1 ) &
}

function findcf()
{
    findcf_dir=$1
    
    if (( "$#" < 1 )) ; then
        findcf_dir='.'
    else
        findcf_dir=$1
    fi
    
    find $findcf_dir -mindepth 1 -type f -name '*.hpp' -o -name '*.C' -o -name '*.c' -o -name '*.cpp' -o -name '*.h' -printf '%p\n' | sort
}

function findf()
{
    findf_dir=$1
    findf_pattern=$2
    
    if (( "$#" != 2 )) ; then
        echo "ERROR: findf requires 2 arguments"
        return
    fi
    
    find ${findf_dir} -type f -name "${findf_pattern}"
}

function fe()
{
    ( /usr/bin/geany $* 1>/dev/null 2>&1 ) &
}

function few()
{
    if (( "$#" -ne 1 )) ; then
        echo "ERROR: few takes only 1 argument"
        return
    fi
    
    warg=$1
    filepath=$(which $warg)
    
    if [[ ! -f $filepath ]] ; then
        echo "ERROR: $warg not found in PATH. Which results:"
        which $warg
        return
    fi
    
    fe $filepath
}

function go()
{
    todir=$1

    if (( "$#" > 1 )) ; then
        echo "ERROR: go takes only 0 or 1 arguments"
        return
    elif (( "$#" == 0 )) ; then
        todir=$HOME
    fi
    
    if [[ ! -d $todir ]] ; then
        echo "ERROR: $1 is not a directory"
        return
    fi

    cd $1
    ls
}

function fm()
{
    todir=$1
    
    if (( "$#" == 0 )) ; then
        todir="."
    fi
    
    if [[ ! -d $todir ]] ; then
        echo "ERROR: $1 is not a directory"
        return
    fi
    
    ( thunar $todir 1>/dev/null 2>&1 ) &
}

function up()
{
    upcnt=0
    
    if [[ -z "$#" ]] ; then
        echo "ERROR: How'd you even do that?"
        return
    elif (( "$#" < 1 )) ; then
        upcnt=1
    else
        upcnt=$1
    fi

    ups=""
    for i in `seq 1 $upcnt` ; do
        ups="$ups../"
    done

    go $ups
}

function pdfunc ()
{
    if (( "$#" < 1 )) ; then
        echo "Usage: <file>"
        echo "    Uses ghostscript to rewrite the file without encryption."
        return
    fi
        
    fullfile="$1"
    fulldir=$(dirname "$fullfile")
    filename=$(basename -- "$fullfile")
    extension="${filename##*.}"
    filename="${filename%.*}"
    
    if [[ "$extension" != "pdf" ]] ; then
        echo "ERROR: \"$fullfile\" is not a pdf"
        return
    elif [[ ! -f $fullfile ]] ; then
        echo "ERROR: \"$fullfile\" is not found"
        return
    fi

    gs -q -dNOPAUSE -dBATCH -sDEVICE=pdfwrite -sOutputFile="$fulldir/${filename}_unencrypted.pdf" -c .setpdfwrite -f "$fullfile"
}

if [ -f "${user_rc_file}" ] ; then
    echo "=================== Loading User Resource File ===================="
    #echo "Loading ${user_rc_file}"
	. "${user_rc_file}"
fi
