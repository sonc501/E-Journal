import { useEffect } from "react";
import { useDispatch, useSelector } from "react-redux";
import { useParams } from "react-router-dom";
import { Journal } from "../../../components";
import { getJournalFromMember } from "../../../context/service/journalService";
import { createPaypalPayment } from "../../../context/service/paymentService";

const MemberCheckout = () => {
  const { slug } = useParams();
  const dispatch = useDispatch();
  const {
    member: { journal },
    base: { redirectUrl },
  } = useSelector((state) => state);

  useEffect(() => {
    dispatch(getJournalFromMember({ slug }));
    /*
     * journal
     * price
     * endDate
     * */
  }, [dispatch, slug]);

  if (redirectUrl !== "") {
    window.location.replace(redirectUrl);
  }

  if (Object.keys(journal).length > 0) {
    return (
      <div>
        <Journal journal={journal} type="full" />
        <p>Price: 10000</p>
        <p>End date: 10-12-2023</p>
        <button
          className="btn"
          onClick={(e) => {
            e.preventDefault();
            dispatch(createPaypalPayment(journal));
          }}
        >
          Pay with paypal
        </button>
      </div>
    );
  }

  return <></>;
};

export default MemberCheckout;
