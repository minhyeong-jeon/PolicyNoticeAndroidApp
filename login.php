<?php  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $userPass=isset($_POST['userPass']) ? $_POST['userPass'] : '';
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    
    //POST 방식으로 전달받은 값을 사용하여 SELECT문을 실행
    if ($userPass != "" ){  //비밀번호가 입력되면
        
        $sql="select * from user where userID='$userID'";  //user테이블에서 userID와 userPass 조회
        $stmt = $con->prepare($sql);
        $stmt->execute();

        //결과데이터가 없으면 에러를 출력하고 데이터를 얻으면 배열을 생성
        if ($stmt->rowCount() == 0){

            //echo "'";
            echo $userID,", ",$userPass;
            echo "일치하는 정보가 없습니다.";
        }
        while(($row=$stmt->fetch(PDO::FETCH_ASSOC))){
            $hash=$row['userPass'];

            if(password_verify($userPass, $hash)){  
                $data = array(); 
        

                extract($row);

                    array_push($data, 
                        array(
                        'userID'=>$row["userID"],
                        'userPass'=>$row["userPass"]
                    ));
                //}

                //안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코
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
            else{
                echo "일치하는 정보가 없습니다.";
            }
        }
        
        
        
    }
    

    else {
        echo "아이디와 비밀번호를 입력하세요 ";
    }

?>



<?php

    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    if (!$android){
?>

<?php
    }

?>