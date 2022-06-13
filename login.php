<?php  
    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $userPass=isset($_POST['userPass']) ? $_POST['userPass'] : '';  //로그인창에 입력된 비밀번호를 받아옴
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';  //로그인창에 입력된 아이디를 받아옴
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    
    //POST 방식으로 전달받은 값을 사용하여 SELECT문을 실행
    if ($userPass != "" ){  //비밀번호가 입력되면
        
        $sql="select * from user where userID='$userID'";  //user테이블에서 userID를 조건으로하여 모든 컬럼 조회
        $stmt = $con->prepare($sql);
        $stmt->execute();

        //결과데이터가 없으면 에러를 출력하고 데이터를 얻으면 배열을 생성
        if ($stmt->rowCount() == 0){    //컬럼이 없으면

            //echo "'";
            echo $userID,", ",$userPass;
            echo "일치하는 정보가 없습니다.";
        }
        while(($row=$stmt->fetch(PDO::FETCH_ASSOC))){
            $hash=$row['userPass'];

            if(password_verify($userPass, $hash)){  //암호화된 비밀번호의 유효성 검사
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