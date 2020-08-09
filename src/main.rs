use warp::Filter;

#[tokio::main]
async fn main() {
    let echo = warp::path!("echo" / String).map(|name| format!("Echo: {}", name));
    
    warp::serve(echo).run(([127, 0, 0, 1], 8080)).await;
}
