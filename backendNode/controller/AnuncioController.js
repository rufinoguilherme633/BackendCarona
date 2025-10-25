const express = require("express");
router = express.Router();
const { cadastrar , divulgar,login, atualizar_anuncio, deletar} = require("../service/AnuncioService");
require("dotenv").config()
const jwt = require("jsonwebtoken")

function checkToken (req,res,next){
  
  const authHEaderv = req.headers["authorization"]
  
  const token = authHEaderv && authHEaderv.split(" ")[1]
    
    if(!token){
        return  res.status(403).json({message: "acesso negado" })
    }

    try {
        jwt.verify(token,process.env.SECRET)
        next()
    } catch (error) {
         res.status(400).json({message:"token invalido"})
    }
}


router.post("/login", async (req,res)=>{

  const body = {
    email : req.body.email,
    senha: req.body.senha
  }

  console.log(body)
  const results = await login(body);

  
  if (results.sucesso) {
    res.status(results.status_code).json({data:results.data});
  } else {
    res.status(results.status_code).json({ message: results.message });
  }
})
router.post("/", async (req, res) => {

  console.log("logo"+ req.body.logo)
  const cadastro ={
    nome_dono: req.body.nome_dono,
    logo: req.body.logo,
    razao_social: req.body.razao_social,
    nome_fantasia: req.body.nome_fantasia,
    cnpj: req.body.cnpj,
    ramo_empresa: req.body.ramo_empresa,
    descricao_anuncio: req.body.descricao_anuncio,
    endereço_empresa: req.body.endereço_empresa,
    endereço_dono: req.body.endereço_dono,
    contato: req.body.contato,
    email: req.body.email,
    senha: req.body.senha,
    anuncio: req.body.anuncio, // anuncio ou video
    quantidade_alcance: req.body.quantidade_alcance,
    
  };

  console.log("Controller", cadastro)
  const results = await cadastrar(cadastro);

  if (results.sucesso) {
    res.status(results.status_code).json({ message: "criado com sucesso" });
  } else {
    res.status(results.status_code).json({ error: results.message });
  }
});



router.get("/divulgar", async (req,res)=>{

  const results = await divulgar();

  
  if (results.sucesso) {
    res.status(results.status_code).json(results.data);
  } else {
    res.status(results.status_code).json({ message: results.message });
  }
})



router.put("/", checkToken, async (req,res)=>{




    const anuncio ={
    nome_dono: req.body.nome_dono,
    logo:req.body.logo,
    razao_social: req.body.razao_social,
    nome_fantasia: req.body.nome_fantasia,
    cnpj: req.body.cnpj,
    ramo_empresa: req.body.ramo_empresa,
    descricao_anuncio: req.body.descricao_anuncio,
    endereço_empresa: req.body.endereço_empresa,
    endereço_dono: req.body.endereço_dono,
    contato: req.body.contato,
    email: req.body.email,
    senha: req.body.senha,
    anuncio: req.body.anuncio, // anuncio ou video
    quantidade_alcance: req.body.quantidade_alcance,
    
  };
  const results = await atualizar_anuncio(anuncio,req.headers["authorization"].split(" ")[1]);

  if (results.sucesso) {
    res.status(results.status_code).json(results.data);
  } else {
    res.status(results.status_code).json({ error: results.message });
  }
})



router.delete("/",checkToken,async (req,res)=>{

  const results = await deletar(req.headers["authorization"].split(" ")[1]);
if (results.sucesso) {
    res.status(results.status_code).json(results.data);
  } else {
    res.status(results.status_code).json({ error: results.message });
  }

})



module.exports = router;
