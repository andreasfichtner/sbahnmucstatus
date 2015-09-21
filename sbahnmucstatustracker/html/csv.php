<?php 
echo 'Please wait...';
shell_exec("sh /home/www/sbahntracker/export.sh"); 
header("Location: export.csv");
die();
?>