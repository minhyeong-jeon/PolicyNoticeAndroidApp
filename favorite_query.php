<?php  

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    //mysql의 favorite 테이블에서 usreID가 같은 데이터를 검색한다.
    $sql="select * from favorite where userID='$userID'";
    $stmt = $con->prepare($sql);
    $stmt->execute();
    
    //favorite 테이블에 해당 userID로 저장된 데이터가 없을 경우
    if ($stmt->rowCount() == 0){

        echo "즐겨찾기를 추가해주세요.";
    }
    else{
        $data = array(); 
        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){

            extract($row);
                //배열에 저장한다.
                array_push($data, 
                    array('userID'=>$row["userID"],
                    'item_name'=>$row["item_name"],
                    'item_content'=>$row["item_content"],
                    'servID'=>$row["servID"],
                    'CloseDt'=>$row["CloseDt"]
                ));

            }


        //안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코
        if (!$android) {
            echo "<pre>";
            //배열을 출력한다.
            print_r($data);
            echo '</pre>';
        }
        else{
            header('Content-Type: application/json; charset=utf8');
            $json = json_encode(array("root"=>$data), JSON_PRETTY_PRINT+JSON_UNESCAPED_UNICODE);
            echo $json;
        }
    }

?>
