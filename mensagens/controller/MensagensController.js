const express = require("express");
const router = express.Router();
const { sendMessage } = require("../service/MensagensService");
const jwt = require("jsonwebtoken")


function checkToken(req, res, next) {
  const authHEaderv = req.headers["authorization"];
  console.log(req.headers["authorization"])
  const token = authHEaderv && authHEaderv.split(" ")[1];

  if (!token) {
    return res.status(403).json({ message: "acesso negado" });
  }

  try {
    jwt.verify(token, process.env.SECRET);
    next();
  } catch (error) {
    console.log(error)
    res.status(400).json({ message: "token invalido" });
  }
}

router.post("/", checkToken, async (req, res) => {
  const message = {
    receiver: req.body.receiver,
    data: req.body.data,
    message: req.body.message,
  };

  console.log(message)
  const results = await sendMessage(req.headers["authorization"].split(" ")[1],message);
  if (results.sucesso) {
    res.status(results.status_code).json({data:results.data});
  } else {
    res.status(results.status_code).json({ error: results.message });
  }
});

module.exports = router;
