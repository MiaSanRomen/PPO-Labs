async function createNewBlock() {
    let status = []
    let id = JSON.parse(localStorage.getItem("current_id"));
    let articles = JSON.parse(localStorage.getItem("articles"));
    let article_id
    for(let i = 0; i < articles.length; i++) {
        let item = articles[i]
        if(item.id == id){
            article_id = i
        }
    }
    let blocks = []
    blocks = articles[article_id].blocks
    let block
    let block_id = JSON.parse(localStorage.getItem("current_block"));
    status.push(block_id)
    console.log("block id", block_id)


    status.push("before fill")

    let input_title = document.getElementById("title")
    let title = input_title.value
    if (title.trim() === "" ) {
        title = articles[id].name
    }
    let input_about = document.getElementById("about")
    let about = input_about.value
    if (about.trim() === "") {
        about = articles[id].about
    }
    status.push("after fill")

    for(let i = 0; i < blocks.length; i++) {
        let item = blocks[i]
        if(item.id == block_id){
            status.push(item)

            articles[article_id].blocks[i].name = title
            articles[article_id].blocks[i].about = about

            status.push(articles[article_id].blocks[i].name)
        }
    }

    localStorage.setItem("articles", JSON.stringify(articles));
    localStorage.setItem("status", JSON.stringify(status));
}

function fillData(){
    let id = JSON.parse(localStorage.getItem("current_id"));
    let articles = JSON.parse(localStorage.getItem("articles"));
    for(let i = 0; i < articles.length; i++) {
        let item = articles[i]
        if(item.id == id){
            j = i
        }
    }
    let blocks = []
    blocks = articles[j].blocks
    console.log("article's blocks", articles[j].blocks)
    let block
    let block_id = JSON.parse(localStorage.getItem("current_block"));
    console.log("block id", block_id)

    for(let i = 0; i < blocks.length; i++) {
        let item = blocks[i]
        if(item.id == block_id){
            block = item
        }
    }

    let  title = document.getElementById("title")
    title.placeholder = block.name

    let  about = document.getElementById("about")
    about.placeholder = block.about

}

fillData()
console.log(status)