<?php  

    error_reporting(E_ALL); 
    ini_set('display_errors',1); 

    include('dbcon.php');

    //POST 값을 읽어온다.
    // $item_content=isset($_POST['item_content']) ? $_POST['item_content'] : '';
    // $item_name = isset($_POST['item_name']) ? $_POST['item_name'] : '';

    $userID = isset($_POST['userID']) ? $_POST['userID'] : '';
    $android = strpos($_SERVER['HTTP_USER_AGENT'], "Android");

    
    //$stmt = $con->prepare("select * FROM favorite where userID='$userID'");
    $sql="select * from favorite where userID='$userID'";
    $stmt = $con->prepare($sql);
    $stmt->execute();

    
    if ($stmt->rowCount() == 0){

        echo "즐겨찾기를 추가해주세요.";
    }
    else{
        $data = array(); 
        while($row=$stmt->fetch(PDO::FETCH_ASSOC)){


            extract($row);
                array_push($data, 
                    array('id'=>$row["id"],
                    'item_name'=>$row["item_name"],
                    'item_content'=>$row["item_content"]
                ));

            }


        //안드로이드에 전달하기 위해 JSON 포맷으로 변경 후 에코한다.
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

?>

