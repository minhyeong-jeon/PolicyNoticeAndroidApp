<?php  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    //POST 방식으로 전달받은 값을 사용하여 SELECT문을 실행
    $sql="select * from user where userID='$userID'";
    $stmt = $con->prepare($sql);
    $stmt->execute();
    
    //결과데이터가 없으면 에러를 출력하고 데이터를 얻으면 배열을 생성
    if ($stmt->rowCount() == 0){

        echo "일치하는 정보가 없습니다.";
    }
    else{

        $data = array(); 

            while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

            extract($row);

                array_push($data, 
                    array(
                    'userLifearray'=>$row["userLifearray"],
                    'userTrgterIndvdl'=>$row["userTrgterIndvdl"],
                    'userArea'=>$row["userArea"]
                ));
            }

            //안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코
            if (!$android) {
                echo "<pre>"; 
                print_r($data); 
                echo '</pre>';
            }else
            {
                header('Content-Type: application/json; charset=utf8');
                $json = json_encode(array("root"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
                echo $json;
                
            }
        }

?>



<?php

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if (!$android){
?>

<?php
    }

?>