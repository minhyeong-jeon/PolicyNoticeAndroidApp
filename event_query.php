<?php 

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');
    
    // 안드로이드 코드에서 postParameters에 적은 값으로 데이터를 전달 받는다
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    //ID가 일치하는 event 테이블의 모든 정보를 조회한다.
    $sql="select * from event where userID='$userID'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    if($userID != "")
    {
        //ID가 일치하는 event 테이블의 모든 정보를 조회한다.
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
        
                //array로 데이터 전송
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
