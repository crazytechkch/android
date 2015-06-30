<?php
//if (isset($_GET["title"]) && isset($_GET["message"])) {

//Edit title and message here
//$title = $_GET["title"];
//$message = $_GET["message"];

$title = 'Eric\'s GCM Test';
$message = 'Message oledi receive lah!';

$data = array(
    'title' => $title,
    'message' => $message);

//Change device ids here
$ids = array('APA91bEQCZu8EjtzJwxGXD2vJBbZI6xXsicegDkR9vuz1vGBh1jv-gN4grCsVRC2SXPHQ1OlUZczejtpXgu6jlNyQ94k3w8-7mb86ut4cCieUj2VrMxgHgyKSBe5J5ihFM2bE5uQgKFC8RmHpL_pCjVmJlFX8FgC6w');

echo 'Sending message through GCM<br/>';

sendGcmMessage($data,$ids);

function sendGcmMessage($data,$ids){
    // Change to your API Key
	$apiKey = 'AIzaSyDZFCUdnotr6dIbmv7x-6JBFIavd3iWbY0';
	$url = 'https://android.googleapis.com/gcm/send';
	$post = array ('registration_ids' => $ids,
		'data' => $data);
	$headers = array( 
                        'Authorization: key=' . $apiKey,
                        'Content-Type: application/json'
                    );
	 $ch = curl_init();

    // Set GCM's url
    curl_setopt( $ch, CURLOPT_URL, $url );

    // Set request method to POST
    curl_setopt( $ch, CURLOPT_POST, true );

    // set custom headers
    curl_setopt( $ch, CURLOPT_HTTPHEADER, $headers );

    // set response as string instead of printing
    curl_setopt( $ch, CURLOPT_RETURNTRANSFER, true );

	// temporarily disable ssl verification
	curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);

    // set post data as json
    curl_setopt( $ch, CURLOPT_POSTFIELDS, json_encode( $post ) );

   // execute!!
    $result = curl_exec( $ch );

   // Error?? Siht!
    if ( curl_errno( $ch ) )
    {
        echo 'GCM error: ' . curl_error( $ch );
    }

   curl_close( $ch );

   //Print result
    echo $result;
}
//}
?>