<?php

namespace App\Controller;

use App\Repository\Evenement\EvenementRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Attribute\Route;

final class EvenementFrontController extends AbstractController
{
    
    #[Route('/Evenemnts', name: 'app_front_evenements')]
    public function evenements(EvenementRepository $evenementRepository): Response
    {
        
        $evenements=$evenementRepository->findAll();

        return $this->render('front-home/card.html.twig', [
            'controller_name' => 'FrontController',
            'evenements' => $evenements,
        ]);
    }

}
