#!/bin/bash

look_for_lower_key_score()
{
temp_key_count=$1
for key in ${!key_score[@]}; do
if [[ ${key_score[$key]} -eq $temp_key_count ]]; then
com="echo \`sed -n '$key,$(($key+9))p;$(($key+10))q' $db \`"
eval $com
selected_review_count=$(($selected_review_count+1))
if [[ $selected_review_count -eq $select_limit ]]; then
exit 0
fi
fi
done
look_for_lower_key_score $(($temp_key_count-1))
}

declare -A key_score
RANDOM=$$
keywords=($@) #Get keywords to be searched
keyword_count=$#
db="/usr/share/tomcat8/webapps/foods.txt"
select_limit=20 #Limit to select
max_sample=5000 #number of samples
if [[ $keyword_count -eq 0 ]]; then  #if no arguments, assign a dummy argument "a"
set a
keyword_count=$(($keyword_count+1))
fi
selected_review_count=0
no_of_lines=`wc -l $db | cut -d" " -f1`
for((samp_count = 1; samp_count < $max_sample ; samp_count++));
do
sample=$(($RANDOM%$no_of_lines))
sample=$(($sample-$(($sample%9)))) #generate a random number representing a review
com="echo \`sed -n '$sample,$(($sample+9))p;$(($sample+10))q' $db \`"
review=`eval $com` #get the sample review
key_score[$sample]=0
for key in ${keywords[@]} #check all the keywords in the review an assign a score
do
    if [[ `echo $review | grep -i -c $key` -gt 0 ]]; then
key_score[$sample]=$((${key_score[$sample]}+1));
fi
done
if [[ ${key_score[$sample]} -eq $keyword_count ]]; then #if all keys are in the review, mark it as selected
selected_review_count=$(($selected_review_count+1))
echo $review
if [[ $selected_review_count -eq $select_limit ]]; then #if the limit is reached exit the program
exit 0
fi
fi
done
look_for_lower_key_score $(($keyword_count-1)) #look for lower scores if top scores are not found
