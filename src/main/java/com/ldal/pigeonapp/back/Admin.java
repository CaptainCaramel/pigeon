package com.ldal.pigeonapp.back;

public class Admin extends User
{
    Admin(int id, String login, String email) {
        super(id, login, email);
    }


    @Override
    public boolean isAdmin(){
        return true;
    }

}
