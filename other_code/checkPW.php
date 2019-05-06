<?php

$con = mysqli_connect("db.sice.indiana.edu", "i494f18_team72", "my+sql=i494f18_team72", "i494f18_team72");

if (mysqli_connect_errno($con)){
    echo "Failed to connect to MySQL:" . mysqli_connect_error();
}

$username = $argv[1];

$result = mysqli_query($con, "SELECT pWord FROM users WHERE uName = '$username'");
$row = mysqli_fetch_array($result);
$data = $row[0];

if($data){
echo $data;
} else {
echo "User doesn't exist";
}

mysqli_close($con);
?>
