<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
        
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");


    $sql="select * from event where userID='$userID'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if($userID != "")
    {
        $sql="select * from event where userID='$userID'";
        $stmt = $con->prepare($sql);
        $stmt->execute();

        if($stmt->rowCount() == 0){
            echo "'";
            echo $userID;
            echo "'은 찾을 수 없습니다.";
        }
        else{
            $data = array(); 

            while($row=$stmt->fetch(PDO::FETCH_ASSOC))
            {
                extract($row);
        
                array_push($data, 
                    array(
                    'ID'=>$ID,
                    'title'=>$title,
                    'startdate'=>$startdate,
                    'enddate'=>$enddate,
                    'alarmactive'=>$alarmactive

                ));
            }

            if (!$android) {
                echo "<pre>";
                print_r($data);
                echo '</pre>';
            }
            else{
                header('Content-Type: application/json; charset=utf8');
                $json = json_encode(array("root"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;
            }
        }
    }
?>
